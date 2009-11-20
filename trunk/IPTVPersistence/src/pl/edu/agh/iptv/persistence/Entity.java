package pl.edu.agh.iptv.persistence;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public class Entity{
	
	Long id;
	int version;
	
	public Entity()
	{
	}
	
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	
	
	@Version
	@Column(name="version")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public void setId(Long id){
		this.id = id;
	}
	
	public int hashCode() {
		return getId().intValue();
	}

	public boolean equals(Object obj) {
		if (obj instanceof Entity) {
			return getId().equals(((Entity)obj).getId());
		}
		
		return super.equals(obj);
	}
}