const mongoose = require('mongoose');

mongoose
    .connect('mongodb://127.0.0.1:27017/todoappdb')
    .then(() => console.log("Connected"))
    .catch(() => console.log("Error"))

// auth schema
const userSchema = new mongoose.Schema({
    email: {
        type: String,
        required: true,
        unique: true
    },
    pass: {
        type: String,
        requied: true
    }
});

const user = mongoose.model('user', userSchema, 'User'); 

module.exports = user;
