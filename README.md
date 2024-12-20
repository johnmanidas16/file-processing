#File-processing
Design big file processing api
Buffered Reading with Fixed Chunk Size:

The BufferedReader with a custom buffer size (CHUNK_SIZE = 8192) ensures that the file is read in manageable chunks, minimizing memory usage.
Batch Processing:

The code processes data in batches (BATCH_SIZE = 1000), reducing memory requirements by not holding the entire file content in memory.

#Streaming Approach:
Used a streaming-based approach for both reading the file and processing records.

#Batch Processing:
Processed batches of fixed size with a String[] array, avoiding ArrayList overhead.

#Controlled Parallelism:
Used a FixedThreadPool executor, limiting the number of threads to match available CPU cores.

The time taken to process a 10GB file depends on several factors, such as:

#Disk I/O Speed:
The speed at which the file can be read from disk.
A standard SSD can read at around 500 MB/s, while HDDs are slower (100-200 MB/s).

#Batch Processing Speed:
The time it takes to process each batch, including parsing records and saving to the database.

#Database Write Speed:
The database's capability to save batches of records efficiently.

#Thread Pool Size:
The number of threads in the ExecutorService impacts parallelism and total processing time.

#Number of Records:
The size of each record determines how many records exist in 10GB of data.

Approximate Calculation:
1. Assumptions:
File Size: 10GB
Record Size: Assume average record size = 100 bytes (this means ~100 million records in 10GB).
Disk Read Speed: ~100 MB/s (HDD) or ~500 MB/s (SSD).
Batch Size: 1000 records.
Database Write Time: Assume 10ms per batch.
Threads in ExecutorService: 8 threads (for an 8-core CPU).

#Number of Batches:
For 100 million records and a batch size of 1000:

#Database Processing Time:
For 1 batch: ~10ms.
For 100,000 batches and 8 threads:

#Total Time:
For HDD:
Total Time = Reading Time+Database Processing Time=100s+125s=225seconds

For SSD:
Total Time=20s+125s=145seconds

Final Estimation:
With HDD: ~3.75 minutes (~225 seconds).
With SSD: ~2.5 minutes (~145 seconds).

