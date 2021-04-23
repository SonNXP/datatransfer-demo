package datatransfer.event;

public enum Target {
    Backend_Small(0x01), Backend_RTM(0x06), Backend(0x07), Backend_Large(0x11), Unknown(255);

    private int target;

    Target(int target) {
        this.target = target;
    }

    /*
     * MyEnum.values()[x] is an expensive operation. If the performance is a
     * concern, should use getInstance() method
     */
    public static Target getInstance(int value) {
        switch (value) {
        case 1:
            return Backend_Small;
        case 2:
            return Backend_RTM;
        case 7:
            return Backend;
        case 0x11:
            return Backend_Large;
        default:
            return Unknown;
        }
    }

    public boolean equal(int target) {
        if (this.target == target) {
            return true;
        } else {
            return false;
        }
    }
}
