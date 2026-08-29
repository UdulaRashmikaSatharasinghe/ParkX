public class ParkingSpace {

    private String slotId;
    private String floor;
    private boolean occupied;
    private Vehicle vehicle;

    public ParkingSpace(
            String slotId,
            String floor) {

        this.slotId = slotId;
        this.floor = floor;
        this.occupied = false;
        this.vehicle = null;
    }

    public String getSlotId() {
        return slotId;
    }

    public String getFloor() {
        return floor;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void occupy(
            Vehicle vehicle) {

        this.vehicle = vehicle;
        this.occupied = true;
    }

    public void release() {
        this.vehicle = null;
        this.occupied = false;
    }
}
