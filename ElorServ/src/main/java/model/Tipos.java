package model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "tipos")
public class Tipos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "name_eu", length = 50)
    private String nameEu;

    // Constructores
    public Tipos() {
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEu() {
        return nameEu;
    }

    public void setNameEu(String nameEu) {
        this.nameEu = nameEu;
    }

	@Override
	public int hashCode() {
		return Objects.hash(id, name, nameEu);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tipos other = (Tipos) obj;
		return Objects.equals(id, other.id) && Objects.equals(name, other.name) && Objects.equals(nameEu, other.nameEu);
	}

	@Override
	public String toString() {
		return "Tipos [id=" + id + ", name=" + name + ", nameEu=" + nameEu + "]";
	}
    
    

}
