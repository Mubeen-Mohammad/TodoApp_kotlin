const express = require('express') 
const app = express() 
const port = process.env.PORT || 3000;
const bodyparser = require('body-parser');
const dd = require('./to/user');
const todo = require('./to/todos');
const { Query } = require('mongoose');
app.use(bodyparser.json());

app.get('/', (req, res) => res.send('Welcome to ToDo App'))

// Login
app.post("/login", async (req, res) => {
    const body = req.body;
    const email = body.email;
    const pass = body.pass;

    const result = await dd.find({"email": email, "pass": pass});
    if(result.length == 1) {
        res.json({msg: "Login successfull", status: 200})
    } else {
        res.json({msg: "Login Failed", status: 400})
    }
})

// register
app.post("/register", async (req, res) => {
    const body = req.body;

    const result = await dd.create(body);
    res.status(201).json({msg: "user registered successfully"})
})


// todos
app.get('/alltodos', async (req, res) => {
    const { email } = req.query;
    try {
        const todos = await todo.find({ email: email });
        res.json(todos);
    } catch (err) {
        res.status(500).json({ message: err.message });
    }
});

// Create a new todo
app.post('/alltodos', async (req, res) => {
    const body = req.body;

    const result = await todo.create( body);
    res.status(201).json({msg: "ToDo submitted successfully"})


});

app.put('/alltodos/:email', async (req, res) => {
    try {
        const updatedTodo = await todo.findOneAndUpdate({ email: req.params.email }, req.body, { new: true });
        res.status(200).json(updatedTodo);
    } catch (err) {
        res.status(400).json({ message: err.message });
    }
});

// Delete a todo
app.delete('/alltodos/:email', async (req, res) => {
    try {
        await todo.findOneAndDelete({ email: req.params.email });
        res.status(200).json({ message: 'Todo deleted' });
    } catch (err) {
        res.status(400).json({ message: err.message });
    }
});

// Start the server
app.listen(port, () => {
    console.log(`Server is running on port ${port}`);
});


