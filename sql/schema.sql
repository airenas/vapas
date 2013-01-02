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
	
	-- drop table saskaitos
    create table Saskaitos (
        Id bigint not null,
        arSaskaita bit not null,
        numeris varchar(100) not null,
        imoneId bigint not null,
        tiekejasId bigint null,
        data timestamp not null,
        sumaSuPvm numeric not null,
        sumaBePvm numeric not null,
        sumaPvm numeric not null,
        prekiuKiekis integer not null,
        statusas integer,
        primary key (Id),
        unique (numeris),
        FOREIGN KEY (imoneId) REFERENCES Imones(Id),
        FOREIGN KEY (tiekejasId) REFERENCES Tiekejai(Id)
    );
-- drop table SaskaitosPrekes
    create table SaskaitosPrekes (
        Id bigint not null,
        serija varchar(100) not null,
        saskaitaId bigint not null,
        prekeId bigint not null,
        matavimoVienetasId bigint not null,
        kiekis numeric not null,
        galiojaIki timestamp,
        kainaBePvm numeric,
        nuolaidosProc numeric,
        pvm numeric,
        sumaPvm numeric,
        sumaSuPvm numeric,
        sumaBePvm numeric,
        primary key (Id),
        FOREIGN KEY (matavimoVienetasId) REFERENCES MatavimoVienetas(Id),
        FOREIGN KEY (prekeId) REFERENCES Prekes(Id),
        FOREIGN KEY (saskaitaId) REFERENCES SASKAITOS(ID)
    );
-- drop view vSaskaitos
create view vSaskaitos as 
select s.Id, s.arSaskaita, s.numeris, s.SUMASUPVM, s.STATUSAS, s.DATA, i.PAVADINIMAS as imone, t.PAVADINIMAS as tiekejas
	from saskaitos s 	left join imones i on s.IMONEID = i.id
					left join TIEKEJAI t on s.TIEKEJASID = t.id;

--drop table likuciai
create table Likuciai (
        Id bigint not null,
        dokumentas varchar(100) not null,
        irasoId bigint,
        prekeId bigint not null,
        imoneId bigint not null,
        matavimoVienetasId bigint not null,
        kiekis numeric not null,
        galiojaIki timestamp,
        serija varchar(100),
        data timestamp not null,
        arSaskaita bit not null,
        primary key (Id),
		FOREIGN KEY (matavimoVienetasId) REFERENCES MatavimoVienetas(Id),
        FOREIGN KEY (prekeId) REFERENCES Prekes(Id) ,
        FOREIGN KEY (imoneId) REFERENCES Imones(Id)
    );

    -- drop view vLikuciaiInt
    -- select * from vLikuciaiInt
create view vLikuciaiInt as 
select l.PREKEID, min(l.MATAVIMOVIENETASID) as MATAVIMOVIENETASID, l.IMONEID, sum(l.KIEKIS) as kiekis, max(l.DATA) as data, min(l.id) as id, sum(Case When l.KIEKIS > 0 Then l.KIEKIS Else 0 End) as pajamuota
	from likuciai l group by l.prekeid, l.imoneid;

  -- drop view vLikuciai
    -- select * from vLikuciai
create view vLikuciai as 
select l.Id, l.kiekis, l.PAJAMUOTA, l.DATA, i.PAVADINIMAS as imone, mv.KODAS as matavimovienetas, p.PAVADINIMAS as preke
	from vLikuciaiInt l left join imones i on l.IMONEID = i.id
					left join Prekes p on l.PREKEID = p.id
					left join MATAVIMOVIENETAS mv on mv.id = l.MATAVIMOVIENETASID;


create table Nurasymai (
        Id bigint not null,
        numeris varchar(100) not null,
        imoneId bigint not null,
        data timestamp not null,
        prekiuKiekis integer not null,
        statusas integer,
        primary key (Id),
        FOREIGN KEY (imoneId) REFERENCES Imones(Id)
    );

    create table NurasymoPrekes (
        Id bigint not null,
        serija varchar(100) not null,
        nurasymasId bigint not null,
        prekeId bigint not null,
        matavimoVienetasId bigint not null,
        kiekis numeric not null,
        primary key (Id),
        FOREIGN KEY (matavimoVienetasId) REFERENCES MatavimoVienetas(Id),
        FOREIGN KEY (prekeId) REFERENCES Prekes(Id)
    );

 -- drop view vNurasymai
create view vNurasymai as 
select n.Id, n.numeris, n.STATUSAS, n.DATA, i.PAVADINIMAS as imone
	from NURASYMAI n 	left join imones i on n.IMONEID = i.id;  

		