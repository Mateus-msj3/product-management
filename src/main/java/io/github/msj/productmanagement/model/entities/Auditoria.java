package io.github.msj.productmanagement.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.msj.productmanagement.config.AuditoriaRevisionListener;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

@Entity
@Table(name = "auditoria")
@RevisionEntity(AuditoriaRevisionListener.class)
@AttributeOverrides({
        @AttributeOverride(name = "timestamp", column = @Column(name = "data_hora")),
        @AttributeOverride(name = "id", column = @Column(name = "auditoria_id"))
})
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Auditoria extends DefaultRevisionEntity {

    @Column(name = "usuario", nullable = false, length = 50)
    private String usuario;

}
