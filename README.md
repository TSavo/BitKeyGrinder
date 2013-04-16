BitKeyGrinder
=============

A Map/Reduce function written in Java 1.6 for generating the Bitcoin public address hash out of phrases (aka Brain Wallets) from some text store, suitable for running on Amazon's Elastic Map Reduce.

To use:

Start by getting BitcoinJ: https://code.google.com/p/bitcoinj/ It's necessary in the final step where we compare the computed hashes to the BlockChain.
Generate a jar file containing all the dependencies, specifying 'org.tsavo.bitkey.BitKeyGrinder' as the main to execute. I used Eclipse for this, and selected 'unpack and include dependencies'. YMMV with other methods.
Copy the resulting .jar file to an Amazon S3 bucket.
Upload the data that you want to use as seeds for your brain wallets to an S3 bucket as a carrige-return seperated file (like a dictionary/word file).
Start an Elastic Map Reduce job.
Specify the jar as '<bucketName>/bitkeygrinder.jar' where <bucketName> is the name of the bucket you uploaded the .jar to.
Specify the parameters to the jar as: 's3n://<bucketName>/<inputFile> s3n://<bucketName>/<outputDirectory>' where <inputFile> is the list of brain wallet phrases, and <outputDirectory> is where you want the result to be stored.
Add some servers, and run the job. Be sure to attach a key you own so you can check on the progress by logging into the box.
Download the results when the job is done, and run the .java main in 'org.tsavo.bitkey.BitKeyChecker' with the path of the directories you want scanned as the argument. You'll need to have the Bitcoin client installed, and the block chain fully downloaded.
The result of that will be a file called 'Brain Wallets', which contains all the brain wallets that have ever been used in the blockchain.