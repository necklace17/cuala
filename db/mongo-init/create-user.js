db.createUser(
    {
      user: "chatdbuser",
      pwd: "mypassword",
      roles: [ { role: "readWrite", db: "chat" } ]
    }
);
