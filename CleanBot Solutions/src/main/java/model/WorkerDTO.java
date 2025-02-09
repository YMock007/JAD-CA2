package model;

public class WorkerDTO {
    private int provider_id;
    private String name;

    public WorkerDTO(int provider_id, String name) {
        this.provider_id = provider_id;
        this.name = name;
    }

    public int getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(int provider_id) {
        this.provider_id = provider_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
