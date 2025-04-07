ALTER TABLE `review`
ADD CONSTRAINT `fk_review_schedule`
    FOREIGN KEY (`schedule_id`) REFERENCES `schedule` (`id`);