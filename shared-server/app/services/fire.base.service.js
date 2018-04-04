const keyFilename="./private_key.json";
const projectId = "storageservice-d467a"
const bucketName = `${projectId}.appspot.com`;
//https://mzmuse.com/blog/how-to-upload-to-firebase-storage-in-node
const mime = require('mime');
const gcs = require('@google-cloud/storage')({
    projectId,
    keyFilename
});

class FireBaseService{
    constructor(){
        var me = this;
        this.upload = () =>{
            const bucket = gcs.bucket(bucketName);
            const filePath = `./this_is_fine.png`;
            const uploadTo = `test/this_is_fine.png`;
            const fileMime = mime.lookup(filePath);
            bucket.upload(filePath, {
                destination: uploadTo,
                public: true,
                metadata: { contentType: fileMime, cacheControl: "public, max-age=300" }
            }, function (err, file) {
                if (err) {
                    console.log(err);
                    return;
                }
                console.log(me.getUrl(uploadTo));
            });
        }
        this.getUrl = (storageName) =>{
            return `http://storage.googleapis.com/${bucketName}/${encodeURIComponent(storageName)}`;
        }
    }
}

module.exports = new FireBaseService();
