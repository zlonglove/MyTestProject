package me.yokeyword.indexablerv;

import java.util.Comparator;

/**
 * @author
 */
class InitialComparator<T extends IndexableEntity> implements Comparator<EntityWrapper<T>> {
    @Override
    public int compare(EntityWrapper<T> lhs, EntityWrapper<T> rhs) {
        return lhs.getIndex().compareTo(rhs.getIndex());
    }
}
