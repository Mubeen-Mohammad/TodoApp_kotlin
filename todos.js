const mongoose = require('mongoose');

mongoose
    .connect('mongodb://127.0.0.1:27017/todoappdb')
    .then(() => console.log("Connected"))
    .catch(() => console.log("Error"))

// Claim schema and model
const todoSchema = new mongoose.Schema({

    email : { type: String , required : true },
    title: {type:String , required : true}
   
});

const todos = mongoose.model('Todos', todoSchema);

module.exports = todos;
