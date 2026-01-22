package elorServ.modelo.generator;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class EntityGenerator {
    
    // CONFIGURACIÓN - MODIFICA ESTOS VALORES
    private static final String DB_URL = "jdbc:mysql://localhost:3307/eduelorrieta";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private static final String PACKAGE_NAME = "creacionPojos.entities";
    private static final String OUTPUT_DIR = "C:\\Users\\in2dm3-v\\Documents\\RETO2 - REPOSITORIO\\ElorServ\\ElorServ\\src\\main\\java\\elorServ\\modelo\\entities\\";
    
    public static void main(String[] args) {
        EntityGenerator generator = new EntityGenerator();
        generator.generateEntities();
    }
    
    public void generateEntities() {
        Connection conn = null;
        try {
            // Conectar a la base de datos
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("✓ Conexión exitosa a la base de datos");
            
            DatabaseMetaData metaData = conn.getMetaData();
            
            // Obtener el nombre del esquema de la URL
            String schema = DB_URL.substring(DB_URL.lastIndexOf("/") + 1);
            if (schema.contains("?")) {
                schema = schema.substring(0, schema.indexOf("?"));
            }
            
            // Obtener todas las tablas
            ResultSet tables = metaData.getTables(schema, null, "%", new String[]{"TABLE"});
            
            int count = 0;
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                System.out.println("\n→ Procesando tabla: " + tableName);
                generateEntityClass(conn, metaData, schema, tableName);
                count++;
            }
            
            System.out.println("\n✓ Generación completada: " + count + " entidades creadas en " + OUTPUT_DIR);
            
        } catch (Exception e) {
            System.err.println("✗ Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private void generateEntityClass(Connection conn, DatabaseMetaData metaData, 
                                    String schema, String tableName) throws Exception {
        
        String className = toCamelCase(tableName, true);
        StringBuilder classContent = new StringBuilder();
        
        // Package e imports
        classContent.append("package ").append(PACKAGE_NAME).append(";\n\n");
        classContent.append("import jakarta.persistence.*;\n");
        classContent.append("import java.io.Serializable;\n");
        classContent.append("import java.util.*;\n");
        classContent.append("import java.math.BigDecimal;\n");
        classContent.append("import java.time.*;\n\n");
        
        // Anotaciones de clase
        classContent.append("@Entity\n");
        classContent.append("@Table(name = \"").append(tableName).append("\")\n");
        classContent.append("public class ").append(className).append(" implements Serializable {\n\n");
        classContent.append("    private static final long serialVersionUID = 1L;\n\n");
        
        // Obtener columnas
        ResultSet columns = metaData.getColumns(schema, null, tableName, "%");
        List<ColumnInfo> columnList = new ArrayList<>();
        
        while (columns.next()) {
            ColumnInfo col = new ColumnInfo();
            col.columnName = columns.getString("COLUMN_NAME");
            col.dataType = columns.getInt("DATA_TYPE");
            col.typeName = columns.getString("TYPE_NAME");
            col.columnSize = columns.getInt("COLUMN_SIZE");
            col.isNullable = "YES".equals(columns.getString("IS_NULLABLE"));
            col.isAutoIncrement = "YES".equals(columns.getString("IS_AUTOINCREMENT"));
            columnList.add(col);
        }
        
        // Obtener primary keys
        Set<String> primaryKeys = new HashSet<>();
        ResultSet pkResultSet = metaData.getPrimaryKeys(schema, null, tableName);
        while (pkResultSet.next()) {
            primaryKeys.add(pkResultSet.getString("COLUMN_NAME"));
        }
        
        // Obtener foreign keys
        Map<String, ForeignKeyInfo> foreignKeys = new HashMap<>();
        ResultSet fkResultSet = metaData.getImportedKeys(schema, null, tableName);
        while (fkResultSet.next()) {
            String fkColumn = fkResultSet.getString("FKCOLUMN_NAME");
            ForeignKeyInfo fkInfo = new ForeignKeyInfo();
            fkInfo.referencedTable = fkResultSet.getString("PKTABLE_NAME");
            fkInfo.referencedColumn = fkResultSet.getString("PKCOLUMN_NAME");
            foreignKeys.put(fkColumn, fkInfo);
        }
        
        // Generar atributos
        for (ColumnInfo col : columnList) {
            String fieldName = toCamelCase(col.columnName, false);
            String javaType = sqlTypeToJavaType(col.dataType, col.typeName);
            
            // Anotaciones del campo
            if (primaryKeys.contains(col.columnName)) {
                classContent.append("    @Id\n");
                if (col.isAutoIncrement) {
                    classContent.append("    @GeneratedValue(strategy = GenerationType.IDENTITY)\n");
                }
            }
            
            classContent.append("    @Column(name = \"").append(col.columnName).append("\"");
            if (!col.isNullable && !primaryKeys.contains(col.columnName)) {
                classContent.append(", nullable = false");
            }
            if (javaType.equals("String") && col.columnSize > 0) {
                classContent.append(", length = ").append(col.columnSize);
            }
            classContent.append(")\n");
            
            // Relación ManyToOne si es FK
            if (foreignKeys.containsKey(col.columnName)) {
                ForeignKeyInfo fkInfo = foreignKeys.get(col.columnName);
                String referencedClass = toCamelCase(fkInfo.referencedTable, true);
                classContent.append("    @ManyToOne\n");
                classContent.append("    @JoinColumn(name = \"").append(col.columnName).append("\")\n");
                classContent.append("    private ").append(referencedClass).append(" ")
                           .append(toCamelCase(fkInfo.referencedTable, false)).append(";\n\n");
                continue;
            }
            
            classContent.append("    private ").append(javaType).append(" ").append(fieldName).append(";\n\n");
        }
        
        // Constructores
        classContent.append("    // Constructores\n");
        classContent.append("    public ").append(className).append("() {\n");
        classContent.append("    }\n\n");
        
        // Getters y Setters
        classContent.append("    // Getters y Setters\n");
        for (ColumnInfo col : columnList) {
            if (foreignKeys.containsKey(col.columnName)) continue;
            
            String fieldName = toCamelCase(col.columnName, false);
            String javaType = sqlTypeToJavaType(col.dataType, col.typeName);
            String capitalizedField = capitalize(fieldName);
            
            // Getter
            classContent.append("    public ").append(javaType).append(" get").append(capitalizedField).append("() {\n");
            classContent.append("        return ").append(fieldName).append(";\n");
            classContent.append("    }\n\n");
            
            // Setter
            classContent.append("    public void set").append(capitalizedField)
                       .append("(").append(javaType).append(" ").append(fieldName).append(") {\n");
            classContent.append("        this.").append(fieldName).append(" = ").append(fieldName).append(";\n");
            classContent.append("    }\n\n");
        }
        
        // Getters y Setters para FKs
        for (Map.Entry<String, ForeignKeyInfo> entry : foreignKeys.entrySet()) {
            ForeignKeyInfo fkInfo = entry.getValue();
            String referencedClass = toCamelCase(fkInfo.referencedTable, true);
            String fieldName = toCamelCase(fkInfo.referencedTable, false);
            String capitalizedField = capitalize(fieldName);
            
            classContent.append("    public ").append(referencedClass).append(" get").append(capitalizedField).append("() {\n");
            classContent.append("        return ").append(fieldName).append(";\n");
            classContent.append("    }\n\n");
            
            classContent.append("    public void set").append(capitalizedField)
                       .append("(").append(referencedClass).append(" ").append(fieldName).append(") {\n");
            classContent.append("        this.").append(fieldName).append(" = ").append(fieldName).append(";\n");
            classContent.append("    }\n\n");
        }
        
        // Cerrar clase
        classContent.append("}\n");
        
        // Escribir archivo
        saveToFile(className, classContent.toString());
    }
    
    private String sqlTypeToJavaType(int sqlType, String typeName) {
        return switch (sqlType) {
            case Types.INTEGER, Types.SMALLINT, Types.TINYINT -> "Integer";
            case Types.BIGINT -> "Long";
            case Types.DECIMAL, Types.NUMERIC -> "BigDecimal";
            case Types.FLOAT, Types.REAL -> "Float";
            case Types.DOUBLE -> "Double";
            case Types.BOOLEAN, Types.BIT -> "Boolean";
            case Types.DATE -> "LocalDate";
            case Types.TIME -> "LocalTime";
            case Types.TIMESTAMP -> "LocalDateTime";
            case Types.BLOB, Types.BINARY, Types.VARBINARY, Types.LONGVARBINARY -> "byte[]";
            default -> "String";
        };
    }
    
    private String toCamelCase(String input, boolean capitalizeFirst) {
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = capitalizeFirst;
        
        for (char c : input.toCharArray()) {
            if (c == '_' || c == '-') {
                capitalizeNext = true;
            } else {
                if (capitalizeNext) {
                    result.append(Character.toUpperCase(c));
                    capitalizeNext = false;
                } else {
                    result.append(Character.toLowerCase(c));
                }
            }
        }
        
        return result.toString();
    }
    
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    
    private void saveToFile(String className, String content) throws IOException {
        File dir = new File(OUTPUT_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        File file = new File(OUTPUT_DIR + className + ".java");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
        System.out.println("  ✓ Generado: " + className + ".java");
    }
    
    // Clases auxiliares
    private static class ColumnInfo {
        String columnName;
        int dataType;
        String typeName;
        int columnSize;
        boolean isNullable;
        boolean isAutoIncrement;
    }
    
    private static class ForeignKeyInfo {
        String referencedTable;
        String referencedColumn;
    }
}
