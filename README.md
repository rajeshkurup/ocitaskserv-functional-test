# Functional Tests for OCI Task Service APIs

- This project contains only Functional Tests for OCI Task Service APIs.
- Functional Tests would be executed on a predefined test environment running in OCI cloud when the project is built.
- This project would not be starting and running as it is meant only for testing OCI Task Service APIs.

## Build Project Using OCI

1. Logon to [OCI Cloud](https://cloud.oracle.com).
2. Launch OCI Visual Builder and Configure OCI Account.
3. Create Build Executor Template for Oracle Linux 8 (Select Required Software, Java-17 and Docker-20).
4. Create Build Executor using above Template.
5. Create Build Project and select above Build Executor Template.
6. Configure Git Repository.
7. Create Build Job.
8. Add Steps to build like Maven Build and Verify (`mvn clean install` and `mvn clean verify -P all-tests`).

## Build Project Manually

### Build

Run `mvn clean install` from root folder.

### Test

Run `mvn clean verify -P all-tests` from root folder.
