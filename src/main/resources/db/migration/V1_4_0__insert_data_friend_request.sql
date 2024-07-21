--  user 12th to 22th send requets to test user --
INSERT INTO friend_requests (sender_id, recipient_id, status)
(
    SELECT id, '4c0670ba-5556-4022-aead-00efe5d07290' , 'PENDING'
    FROM users
    OFFSET 11 LIMIT 10
)


