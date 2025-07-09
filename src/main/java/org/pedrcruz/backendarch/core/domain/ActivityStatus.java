package org.pedrcruz.backendarch.core.domain;

import eapli.framework.domain.model.ValueObject;
import jakarta.persistence.Embeddable;
import lombok.Value;
import lombok.experimental.Accessors;


@Embeddable
@Value
@Accessors(fluent = true)
public class ActivityStatus implements ValueObject, Comparable<ActivityStatus>{

    private static final long serialVersionUID = 1L;

    public static final ActivityStatus ACTIVE = new ActivityStatus(true);
    public static final ActivityStatus INACTIVE = new ActivityStatus(false);

    boolean status;

    public ActivityStatus(boolean status) {
        this.status = status;
    }

    protected ActivityStatus() {
        status = false;
    }

    public boolean isActive() {
        return this.status;
    }

    @Override
    public String toString() {
        return status ? "Active" : "Inactive";
    }

    @Override
    public int compareTo(ActivityStatus o) {
        if (this.status && !o.status) {
            return 1;
        } else if (!this.status && o.status) {
            return -1;
        } else {
            return 0;
        }
    }
}