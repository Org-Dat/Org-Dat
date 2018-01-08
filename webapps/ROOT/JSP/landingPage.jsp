<!doctype html>

<html>
    <head>
        
    </head>
    <body>
        <form action='/signup' method='post'>
            <h1>SignUp</h1><pre>
            Name      : <input type='text' name='name' /><br>
            Email     : <input type='text' name='email' /><br>
            password  : <input type='password' name='password' /><br>
            phone     : <input type='number' name='phone_number' /><br>
            <input type='submit' /></pre>
        </form>
        <form action='/signin' method='post'>
            <h1>SignIn</h1><pre>
            Email     : <input type='text' name='email' /><br>
            password  : <input type='password' name='password' /><br>
            <input type='submit' /></pre>
        </form>
    </body>
</html>