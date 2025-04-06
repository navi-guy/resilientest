CREATE TABLE IF NOT EXISTS dom_page
(
    id          INTEGER PRIMARY KEY,
    url         varchar(100) NOT NULL,
    description varchar(255) NULL,
    screen_height INTEGER NOT NULL, -- SCREEN SIZE = MONITOR TAMAÑO QUE TIENE EL MONITOR
    screen_width  INTEGER NOT NULL,
    viewport_height INTEGER NOT NULL, -- VIEWPORT = PARTE VISIBLE DE LA PÁGINA WEB
    viewport_width  INTEGER NOT NULL,
    full_page_screenshot_path varchar(100) NOT NULL, -- BY DEFAULT MAXIMIZED// tal vez no sea necesario
    content     TEXT NOT NULL
);



CREATE TABLE IF NOT EXISTS web_element
(
    id                 INTEGER PRIMARY KEY,
    id_attribute       varchar(255) NULL,
    class_name         varchar(255) NULL,
    tag                varchar(255) NOT NULL,
    name               varchar(255) NULL,
    href               varchar(255) NULL,
    alt                varchar(255) NULL,
    full_xpath         varchar(255) NOT NULL,
    relative_xpath     varchar(255) NOT NULL,
    inner_text         varchar(255) NULL,
    location_x         INTEGER NOT NULL,
    location_y         INTEGER NOT NULL,
    width              INTEGER NOT NULL,
    height             INTEGER NOT NULL,
    area               INTEGER GENERATED ALWAYS AS (width * height) STORED,
    shape              REAL GENERATED ALWAYS AS (width / height) STORED,
    is_button          BOOLEAN NOT NULL,
    last_valid_type_selector varchar(255) NULL,
    last_valid_selector varchar(255) NULL,
    element_screenshot_path varchar(100) NOT NULL,
    neighbor_elements varchar(255) NOT NULL,
    neighbor_elements_text varchar(255) NOT NULL,
    dom_page_id        INTEGER NOT NULL,
    FOREIGN KEY (dom_page_id) REFERENCES dom_page (id)
);



