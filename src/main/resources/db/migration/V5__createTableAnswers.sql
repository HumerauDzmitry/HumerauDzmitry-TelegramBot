create table answers (
    id  bigserial not null,
    created_at timestamp not null,
    created_by text not null,
    status text not null,
    updated_at timestamp not null,
    updated_by text not null,
    amount_attractions int,
    text text,
    primary key (id)
)
