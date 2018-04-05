var fs = require('fs');
var moment = require('moment')

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
        this.upload = (fileBase64, cb) =>{
            var pathFile = me.save(fileBase64);
            const bucket = gcs.bucket(bucketName);
            const filePath = __dirname + '/../../' + pathFile;
            const uploadTo = 'test/' + pathFile.split("/")[1];
            const fileMime = mime.lookup(filePath);
            bucket.upload(filePath, {
                destination: uploadTo,
                public: true,
                metadata: { contentType: fileMime, cacheControl: "public, max-age=300" }
            }, function (err, file) {
                cb(err, me.getUrl(uploadTo));
            });
        }
        this.getUrl = (storageName) =>{
            return `http://storage.googleapis.com/${bucketName}/${encodeURIComponent(storageName)}`;
        }
        this.save = (fileBase64) => {
            var type = fileBase64.split("/")[0];
            var extension = (fileBase64.split(";")[0]).split("/")[1];
            var data = fileBase64.split(",")[1];
            var CurrentDate = moment().toDate().getTime();
            var pathFile = "tmp/tmp_"+CurrentDate+"."+extension;
            console.info(CurrentDate);
            require("fs").writeFile(pathFile, data, 'base64', function(err) {
              console.log(err);
            });
            return pathFile;
        }
    }
}

module.exports = new FireBaseService();
