package game;

import java.util.Objects;

public class Movement {
    private Position origin;
    private Position target;

    public Movement(Position origin, Position target) {
        this.origin = origin;
        this.target = target;
    }

    public Movement(int originRow, int originCol, int targetRow, int targetCol) {
        this.origin = new Position(originRow, originCol);
        this.target = new Position(targetRow, targetCol);
    }

    public boolean valid() {    //nieprawwidlowy jest ruch na to samo miejsce
        return origin.valid() && target.valid() && !origin.equals(target);
    }

    public Position getOrigin() {
        return origin;
    }

    public Position getTarget() {
        return target;
    }

    public int getOriginRow() {
        return origin.getRow();
    }

    public int getOriginCol() {
        return origin.getCol();
    }

    public int getTargetRow() {
        return target.getRow();
    }

    public int getTargetCol() {
        return target.getCol();
    }

    public int getRowOffset() {
        return target.getRow() - origin.getRow();
    }

    public int getColOffset() {
        return target.getCol() - origin.getCol();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movement movement = (Movement) o;
        return Objects.equals(origin, movement.origin) &&
                Objects.equals(target, movement.target);
    }
}
