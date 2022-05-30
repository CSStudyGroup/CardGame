alter table card
    change cid category_id bigint not null,
    add column requester_id bigint not null;