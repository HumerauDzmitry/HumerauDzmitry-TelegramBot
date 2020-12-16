create table cities (
    id  bigserial not null,
    created_at timestamp not null,
    created_by text not null,
    status text not null,
    updated_at timestamp not null,
    updated_by text not null,
    name text,
    primary key (id)
);

create table attractions (
    id  bigserial not null,
    created_at timestamp not null,
    created_by text not null,
    status text not null,
    updated_at timestamp not null,
    updated_by text not null,
    name text,
    primary key (id)
);

create table cities_attractions (
    city_id bigint not null,
    attraction_id bigint not null
);

alter table cities_attractions add constraint UK_3vg8iwbnw02016xuevi5191r1 unique (attraction_id);
alter table cities_attractions add constraint FKkrvgru3ejptjjmy3pwexmpiv3 foreign key (attraction_id) references attractions;
alter table cities_attractions add constraint FK369mcj9gn3htwbk9oioaaundq foreign key (city_id) references cities;
