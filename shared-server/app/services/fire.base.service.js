var stream = require('stream');
var moment = require('moment');
var config = require('../../config/default')


const keyFilename   = config.firebase.keyFilename;
const projectId     = config.firebase.projectId
const bucketName    = `${projectId}.appspot.com`;
const gcs = require(config.firebase.storage)({
    projectId,
    keyFilename
});

//ISSUE: https://stackoverflow.com/questions/42879012/how-do-i-upload-a-base64-encoded-image-string-directly-to-a-google-cloud-stora
class FireBaseService{
    constructor(){
        var me = this;
        this.upload = (data, cb) =>{
            var bufferStream = new stream.PassThrough();
            bufferStream.end(new Buffer(data.resource, 'base64'));
            var CurrentDate = moment().toDate().getTime();
            var path = 'test/' + CurrentDate + '_' + data.filename;
            var bucket = gcs.bucket(bucketName);
            var file = bucket.file(path);
            bufferStream.pipe(file.createWriteStream({
                metadata: {
                  contentType: 'image/jpeg',
                  metadata: {
                    custom: 'metadata'
                  }
                },
                public: true,
                destination: path,
                validation: "md5"
              }))
              .on('error', function(err) {})
              .on('finish', function() {
                cb(undefined, me.getUrl(path));
              });
        };
        this.getUrl = (storageName) =>{
            return `http://storage.googleapis.com/${bucketName}/${encodeURIComponent(storageName)}`;
        }
    }
}

module.exports = new FireBaseService();
