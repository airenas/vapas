create table MatavimoVienetas (
        Id bigint not null,
        kodas varchar(20) not null,
        pavadinimas varchar(100) not null,
        pastaba varchar(5000) null,
        primary key (Id),
        unique (kodas)
    );

--drop table prekes
create table Prekes (
        Id bigint not null,
        pavadinimas varchar(100) not null,
        matVienetasId bigint not null,
        pastaba varchar(5000) null,
        primary key (Id),
        unique (pavadinimas),
        FOREIGN KEY (matVienetasId) REFERENCES MatavimoVienetas(Id)
    );

create view vPrekes as 
select p.Id, p.Pavadinimas, v.kodas as matVienetas from prekes p left join MATAVIMOVIENETAS v on p.MATVIENETASID = v.id;

--drop table prekes
create table Tiekejai (
        Id bigint not null,
        pavadinimas varchar(100) not null,
        telefonas varchar(50) not null,
        adresas varchar(200) not null,
        pastaba varchar(5000) null,
        primary key (Id),
        unique (pavadinimas)
    );

    --drop table prekes
create table Imones (
        Id bigint not null,
        pavadinimas varchar(100) not null,
        telefonas varchar(50) not null,
        adresas varchar(200) not null,
        pastaba varchar(5000) null,
        primary key (Id),
        unique (pavadinimas)
    );
 