"use strict"

class DaoService {
    constructor(){
        this.insert = (model, entity) =>{
            return new Promise((resolve, reject) => {
                delete entity.dataValues.id
                if(entity.dataValues.token){
                    var aux = entity.dataValues.token.token
                    delete entity.dataValues.token.token
                    entity.dataValues["token"] = aux
                }
                model.create(entity.dataValues)
                .then(resolve)
                .catch(e =>{
                    console.error(e.name)
                    reject(e.name)
                });
            });
        };
    }

};

module.exports = new DaoService();
