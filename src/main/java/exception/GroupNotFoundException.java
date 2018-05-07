package exception;

public class GroupNotFoundException extends Exception{
    public GroupNotFoundException() {
        super("Group not found!");
    }

    public GroupNotFoundException(String name) {
        super("Group with name: "+name+" not found!");
    }

    public GroupNotFoundException(Long id) {
        super("Group with id: "+id+" not found!");
    }
}
