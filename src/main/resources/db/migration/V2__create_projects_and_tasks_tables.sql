create table projects(
    id bigserial primary key,
    name varchar(200) not null,
    description varchar(1000),
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

create table tasks(
    id bigserial primary key,
    title varchar(300) not null,
    description varchar(2000),
    status varchar(20) not null default 'TODO',
    priority varchar(20) not null default 'Medium',
    due_date date,
    version integer not null default 0,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    project_id bigint not null references projects(id) on delete cascade,
    assignee_id bigint references users(id) on delete set null
);

create index idx_tasks_project_id on tasks(project_id);
create index idx_tasks_assignee_id on tasks(assignee_id);
create index idx_tasks_status on tasks(status);