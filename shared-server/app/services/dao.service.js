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

    }

};

module.exports = new DaoService();
