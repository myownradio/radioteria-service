# Radioteria Web Service

### To run type:
```shell
make run
```

### Profiles:
```shell
File System:
  fs-memory    - Memory-based file system mode
  fs-local     - Local file system mode [--fs.local.root]
  fs-s3        - S3 file system [--fs.s3.bucket, --fs.s3.accessKey, --fs.s3.secretKey]

Database:
  db-embedded  - Embedded database mode (memory-based)
  db-mysql     - MySQL database mode for local development [--db.username, --db.password]
  db-props     - Properties-based database [--db.dialect, --db.driver, --db.url, --db.username, --db.password]

Mail:
  email-log    - Logger-based email service.
  email-props  - Properties-based email service.

Default active profiles: fs-memory, db-embedded.
To override use [--spring.profiles.active] parameter.
```
