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

        this.findAll = (model, include) => {
            return new Promise((resolve, reject) => {
                var params = {};
                if (include){
                    params.include = include;
                }
                model.findAll(params).then(entities => {
                    resolve(entities);
                })
                .catch(e => {
                    console.error(e.name);
                    reject(e.name) 
                });
            });
        }

        this.findById = (id, model, include) => {
            return new Promise((resolve, reject) => {
                var params = {};
                if (include){
                    params.include = include;
                }
                model.findById(id, params).then(entity => {
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
