UPDATE product_option_section SET max_allowed_choices = 1 WHERE max_allowed_choices = 0 OR max_allowed_choices IS NULL;