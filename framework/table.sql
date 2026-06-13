create table if not exists Users(
    id int primary key,
    nom varchar(100),
    prenom varchar(100),
    age int
);

insert into Users (id, nom, prenom, age) values
    (2, 'Smith', 'Alice', 27),
    (3, 'Brown', 'Charlie', 35);