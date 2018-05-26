"use strict"

class DaoService {
    constructor(){
        
        this.insert = (model, attrs) =>{
            return new Promise((resolve, reject) => {
                delete attrs.id
                model.create(attrs)
                .then(newEntity => {
                    resolve(newEntity);
                })
                .catch(e =>{
                    console.error(e.name)
                    reject(e.name)
                });

            });
        };

        this.update = (entity) => {
            return new Promise((resolve, reject) => {
                entity.save().then(entity => {
                    resolve(entity);
                })
                .catch(e => {
                    console.error(e.name)
                    reject(e.name) 
                });
            });
        }

        this.delete = (entity) => {
            return new Promise((resolve, reject) => {
                entity.destroy()
                .then(() => {
                    resolve();
                })
                .catch(e => {
                    console.error(e.name)
                    reject(e.name) 
                });
            });
        }

        this.findAll = (model) => {
            return new Promise((resolve, reject) => {
                model.findAll().then(entities => {
                    resolve(entities);
                })
                .catch(e => {
                    console.error(e.name)
                    reject(e.name) 
                });
            });
        }

        this.findById = (id, model) => {
            return new Promise((resolve, reject) => {
                model.findById(id).then(entity => {
                    if (entity){
                        resolve(entity);    
                    } else {
                        reject("not-found");
                    }
                })
                .catch(e => {
                    console.log("e:");
                    console.error(e.name)
                    reject(e.name) 
                });
            });
        }

        this.findOne = (filter, model) => {
            return new Promise((resolve, reject) => {
                model.findOne({ where: filter }).then(entity => {
                    if (entity){
                        resolve(entity);    
                    } else {
                        reject("not-found");
                    }
                })
                .catch(e => {
                    console.error(e.name)
                    reject(e.name) 
                });
            });
        }
    }

};

module.exports = new DaoService();
