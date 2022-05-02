package fis.marc.domain;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class Manage {
    @Id
    @GeneratedValue
    @Column(name = "manage_id")
    private Long id;

    private Long goal;

    public void updateGoal(Long goal) {
        this.goal = goal;
    }

    protected Manage() {}

    public Manage(Long goal) {
        this.goal = goal;
    }
}
