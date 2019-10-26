package chunks;

public interface Monitor {
    void put(int producerId, int chunksToPut);
    void get(int consumerId, int chunksToGet);
}
