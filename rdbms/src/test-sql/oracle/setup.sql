create table PP (
   a number(8) not null
);
ALTER TABLE PP
 ADD CONSTRAINT PP_PK PRIMARY KEY (a);

create table QQ (
   m number(8) not null,
   primary key (m),
   foreign key (m) references PP(a)
);

create table TT (
   a number(8) not null,
   primary key (a)
);
create table UU (
   m number(8) not null,
   n number(8) not null,
   primary key (m,n),
   foreign key (m) references TT(a),
   foreign key (n) references TT(a)
);

create table VV (
   a number(8) not null,
   b number(8) not null,
   primary key (a,b)
);
create table WW (
   m number(8) not null,
   n number(8) not null,
   primary key (m,n),
   foreign key (m,n) references VV(a,b)
);

create table XX (
   a number(8) not null,
   b number(8) not null,
   c number(8) not null,
   d number(8) not null,
   primary key (a,b)
);
create table YY (
   m number(8) not null,
   n number(8) not null,
   o number(8) not null,
   p number(8) not null,
   primary key (m,n),
   foreign key (m,n) references XX(a,b),
   foreign key (o,p) references XX(a,b)
);

create table ZZ (
   a number(8) not null,
   b varchar(32) not null,
   c number(8) not null,
   d number(8) not null,
   primary key (a,b)
);
