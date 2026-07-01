# Current Behavior Baseline

Phase: 0 - Stabilize the current project before modernization.

Date captured: 2026-06-24

Workspace root:

```text
C:\Users\User\Downloads\Model1(first Year edition) (3)
```

Active project:

```text
C:\Users\User\Downloads\Model1(first Year edition) (3)\Model1 (1)\Model
```

## Phase 0 Status

Completed:

- Created a fresh backup zip of the current project.
- Confirmed the Eclipse project metadata exists.
- Confirmed the dependency paths declared by `.classpath` exist on disk.
- Confirmed the source tree and compiled output are present.
- Captured current database tables, row counts, and receipt file state.
- Documented current credentials and workflows.

Not fully confirmed:

- A live JavaFX launch was not performed from this terminal session. The installed Java launcher is present, but `java -version` and `javac -version` returned exit code `1` with no captured output in this shell environment.
- Manual Eclipse run confirmation is still recommended before Phase 1.

## Backup

Fresh Phase 0 backup:

```text
C:\Users\User\Downloads\Model1(first Year edition) (3)\phase0-backup-20260624-125014.zip
```

Existing packaged archive:

```text
C:\Users\User\Downloads\Model1(first Year edition) (3)\Model1 (1)\Model.zip
```

Git note:

- A `.git` directory exists at the workspace root, but `git status` does not recognize the folder as a valid Git repository.
- Phase 0 therefore uses a zip backup rather than a backup branch.

## Project Type

The project is an Eclipse Java desktop application.

Main traits:

- JavaFX UI.
- SQLite local database.
- JDBC-based persistence.
- Manual Eclipse `.classpath` dependency management.
- MVC-like package split:
  - `view`
  - `control`
  - `model`
  - `main`

There is no Maven or Gradle build file currently.

## Source Structure

Source root:

```text
Model1 (1)\Model\src
```

Java source file count:

```text
44
```

Package counts:

```text
control: 33 files
main:     1 file
model:    4 files
view:     6 files
```

Compiled output:

```text
Model1 (1)\Model\bin
```

Compiled class count:

```text
45
```

## Entry Point

Application entry point:

```text
src\main\Main.java
```

Behavior:

- Calls `Application.launch(view.mainLogin.class, args)`.
- The JavaFX application starts from `view.mainLogin`.

## Dependencies

Dependencies are declared in:

```text
Model1 (1)\Model\.classpath
```

Declared runtime/compiler target:

```text
JavaSE-22
```

Declared external libraries:

```text
C:\Users\User\Downloads\sqlite-jdbc-3.47.1.0.jar
C:\Users\User\Downloads\javafx-sdk-23.0.1\lib\javafx.base.jar
C:\Users\User\Downloads\javafx-sdk-23.0.1\lib\javafx.controls.jar
C:\Users\User\Downloads\javafx-sdk-23.0.1\lib\javafx.fxml.jar
C:\Users\User\Downloads\javafx-sdk-23.0.1\lib\javafx.graphics.jar
C:\Users\User\Downloads\javafx-sdk-23.0.1\lib\javafx.media.jar
C:\Users\User\Downloads\javafx-sdk-23.0.1\lib\javafx.swing.jar
C:\Users\User\Downloads\javafx-sdk-23.0.1\lib\javafx.web.jar
C:\Users\User\Downloads\javafx-sdk-23.0.1\lib\javafx-swt.jar
```

The SQLite JDBC jar and JavaFX controls jar were confirmed to exist at the declared paths.

## Login Credentials From ReadMe

Cashier:

```text
username: kister
password: kister123

username: aleks
password: aleks123
```

Manager:

```text
username: aster
password: aster1234

username: bister
password: bister1234
```

Administrator:

```text
username: dister
password: dister12345

username: bister
password: bister12345
```

Important observed data mismatch:

- The database currently contains `bister` manager password as `bister123`.
- The database currently contains `bister` administrator password as `bister123`.
- The ReadMe lists `bister1234` and `bister12345`.

## Database

Database file:

```text
Model1 (1)\Model\data.db
```

Connection string used by source code:

```text
jdbc:sqlite:data.db
```

This means the app expects to run with the project root as the working directory, otherwise the relative database path may not resolve to the intended file.

### Tables And Row Counts

```text
Administrator: 2 rows
Bill:          15 rows
Cashier:       2 rows
cashier_Login: 40 rows
expenses:      9 rows
Manager:       2 rows
Notifications: 0 rows
Product:       11 rows
Supplier:      5 rows
```

### Tables And Columns

```text
Administrator:
- aId INTEGER
- aName TEXT
- aBirthDate DATE
- aPhoneNumber NUMERIC
- aemail TEXT
- aSalary REAL
- aAccessLevel INTEGER
- aUsername TEXT
- apassword TEXT
- sector INTEGER

Bill:
- BillId NUMERIC
- BillDate DATE
- total BLOB
- fileName TEXT
- cashier TEXT
- totalItems INTEGER

Cashier:
- cId INTEGER
- cName TEXT
- cBirthDate DATE
- cPhoneNumber NUMERIC
- email TEXT
- cSalary REAL
- cAccessLevel INTEGER
- cUsername TEXT
- cPassword TEXT
- Sector INTEGER

cashier_Login:
- cashierName TEXT
- loginId INTEGER

expenses:
- expenseDate DATE
- itemsPurchased INTEGER
- expense BLOB
- expenseSector INTEGER

Manager:
- mId INTEGER
- mName TEXT
- mBirthDate DATE
- mPhoneNumber NUMERIC
- memail TEXT
- mSalary REAL
- mAccessLevel INTEGER
- mUsername TEXT
- mPassword TEXT
- Sector

Notifications:
- NotificationProduct TEXT
- notificationSector INTEGER

Product:
- ProductName TEXT
- Quantity INTEGER
- ProductPrice BLOB
- supplierId INTEGER
- Sector INTEGER

Supplier:
- SupplierId INTEGER
- SupplierName TEXT
- SupplierCategory TEXT
```

## Receipt And Bill Files

Receipt files are stored in the project root as `.txt` files.

Current receipt files on disk:

```text
001042025.txt
1001202025.txt
1101202025.txt
1201212025.txt
1301232025.txt
1301242025.txt
1401242025.txt
501192025.txt
601192025.txt
701202025.txt
801202025.txt
901202025.txt
```

Bill filenames currently stored in the database:

```text
001042025.txt
1001202025.txt
101042025.txt
1101202025.txt
1201212025.txt
1301242025.txt
1401242025.txt
201052025.txt
301052025.txt
401172025.txt
501192025.txt
601192025.txt
701202025.txt
801202025.txt
901202025.txt
```

Database bill files missing on disk:

```text
101042025.txt
201052025.txt
301052025.txt
401172025.txt
```

Disk receipt files not present in database:

```text
1301232025.txt
```

Important code note:

- `control.menu2` uses a hardcoded receipt directory:

```text
C:/Users/User/eclipse-workspace/Model
```

That path is different from the current project path and should be treated as a known portability issue for later phases.

## Current Workflow Map

## Login Screen

Main class:

```text
view.mainLogin
```

User chooses one of three roles:

- Cashier
- Manager
- Administrator

Each role opens a separate login screen.

Authentication methods:

```text
model.Database.checkCredentialsCashier
model.Database.checkCredentialsManager
model.Database.checkCredentialsAdministrator
```

Access-level rules:

```text
Cashier:       caccesslevel > 0
Manager:       maccesslevel > 1
Administrator: aaccesslevel > 2
```

On cashier login, a row is inserted into `cashier_Login`.

## Cashier Workflows

Cashier dashboard options:

- Create new bill.
- View bill statistics.

### Create New Bill

Main screen:

```text
view.Cashier.menu1
```

User enters/selects:

- Product name
- Quantity
- Product sector

Current behavior:

- Product names are loaded from the `Product` table.
- Product field uses an editable `ComboBox`.
- Product list is sorted based on typed prefix.
- Add button checks whether the product exists and has enough quantity.
- Running total is kept in `model.Return`.
- Finish button updates product quantities.
- A receipt text file is written to the working directory.
- A bill row is inserted into the `Bill` table.

Related methods:

```text
control.menu1AddButton.add
control.menu1FinishButton.finish
model.Database.checkProduct
model.updateInsert.updateProductQuantity
model.updateInsert.insertBill
```

Low-stock behavior:

- When product quantity is at or below 5 during stock check, a notification is inserted.

### View Bill Statistics

Main screen:

```text
view.Cashier.menu2
```

Current behavior:

- Loads bill filenames for the current date from the `Bill` table.
- Displays files as JavaFX hyperlinks.
- Displays total bill revenue for the current date.
- Opens selected receipt files with `Desktop.open`.

Related methods:

```text
model.Get.getFileNames
model.Get.getTotalBills
control.menu2.menu2
control.menu2Hyperlink.hyperlink
```

## Manager Workflows

Manager dashboard options:

- Add new product category.
- Add new product.
- View bill statistics.
- Notifications.

### Add New Product Category

Main screen:

```text
view.Manager.menu1
```

User enters:

- Product name
- Product quantity
- Product price
- Product sector
- Supplier id
- Supplier name
- Supplier category

Current behavior:

- Inserts into `Product`.
- Inserts into `Supplier`.
- Inserts purchase expense into `expenses`.

Related method:

```text
model.updateInsert.insertNewProductCategory
```

### Add New Product

Main screen:

```text
view.Manager.menu3
```

User enters:

- Product name
- Product quantity
- Product price
- Product sector
- Supplier id

Current behavior:

- Inserts into `Product`.
- Inserts purchase expense into `expenses`.
- Assumes supplier already exists.

Related method:

```text
model.updateInsert.insertNewProduct
```

### Manager Reports

Main screen:

```text
view.Manager.menu2
```

Report choices:

- Cashier Statistics
- General Statistics

Cashier report inputs:

- Cashier name
- Starting date
- Ending date

Cashier report outputs:

- Total number of bills generated
- Total items sold
- Total revenue

General report inputs:

- Starting date
- Ending date

General report outputs:

- Total number of items sold
- Total number of items purchased
- Items sold revenue
- Items purchased revenue
- Balance

Related methods:

```text
model.Return.returnTotalBills
model.Return.returnTotalItems
model.Return.returnTotalRevenue
model.Return.returnItemsSold
model.Return.returnItemsPurchased
model.Return.returnItemsSoldRevenue
model.Return.returnItemsPurchasedRevenue
```

Known behavior note:

- Some manager general report fields currently ignore the entered date range and use all-time totals.

### Notifications And Refill

Main screen:

```text
view.Manager.menu4
```

Current behavior:

- Manager enters a sector number first.
- App loads notifications for that sector.
- Each notification row shows product name, quantity input, and refill button.
- Refill increases product quantity.
- Refill records an expense.
- Refill clears matching notification rows.

Related methods:

```text
model.Get.getNotifications
model.updateInsert.refill
model.updateInsert.clear
```

## Administrator Workflows

Administrator dashboard options:

- Add employee.
- Edit employee.
- Delete employee.
- View Statistics.

### Add Employee

Main screen:

```text
view.Administrator.menu1
```

Employee types:

- Cashier
- Manager
- Administrator

User enters:

- Id
- Name
- Birthdate
- Phone number
- Email
- Salary
- Access level
- Username
- Password
- Sector

Current behavior:

- Inserts into role-specific table based on chosen add button.

Related methods:

```text
model.updateInsert.insertNewCashier
model.updateInsert.insertNewManager
model.updateInsert.insertNewAdministrator
```

### Edit Employee

Main screen:

```text
view.Administrator.menu2
```

User selects/enters:

- Role
- Field
- New value
- Employee name

Current behavior:

- Builds an SQL update dynamically from role and field.
- Parses new value as `double`.

Related method:

```text
model.updateInsert.edit
```

Known behavior note:

- This currently works best for numeric fields.
- String updates are likely unreliable because `newField` is parsed as `double`.

### Delete Employee

Main screen:

```text
view.Administrator.menu3
```

User selects/enters:

- Role
- Employee name

Current behavior:

- Deletes rows from the selected role table by name.

Related method:

```text
model.updateInsert.delete
```

### Administrator Financial Statistics

Main screen:

```text
view.Administrator.menu4
```

User enters:

- Starting date
- Ending date

Outputs:

- Total revenue
- Total expense
- Balance

Related methods:

```text
model.Return.returnItemsSoldRevenueTimed
model.Return.returnFinalTotalExpense
model.Return.returnItemsSoldRevenue
```

Known behavior note:

- Balance mixes all-time revenue with ranged expense in the current implementation.

## Current Alerts And Validation

The app uses JavaFX alerts for:

- Login failure.
- Empty fields.
- Product add failure.
- Product quantity update failure.
- Generic success/done.
- Missing bill file.
- No bills found.

Known behavior from ReadMe:

- Numeric parsing is handled more consistently than string validation.
- Some alerts are reused and may not show perfectly accurate wording for each situation.

## Known Technical Risks To Preserve For Later Phases

These are not Phase 0 fixes. They are recorded so later migration phases can address them deliberately.

- Database path is relative: `jdbc:sqlite:data.db`.
- Receipt opening path is hardcoded to an old Eclipse workspace.
- Password fields are plain `TextField` controls, not `PasswordField`.
- Money values use `double` or database `BLOB` columns rather than `BigDecimal`.
- Dates are stored as custom numeric/string formats like `MMddyyyy`.
- Bill line items are stored in text files, not normalized into a `bill_items` table.
- Some report calculations use all-time data even when date inputs are present.
- Dynamic SQL is used for administrator edit/delete behavior.
- Several controller classes are thin navigation wrappers.
- `view.administratorLogin` and `view.administratorMenu1` are empty placeholder classes.
- The app depends on machine-specific library paths from `.classpath`.

## Phase 0 Acceptance Criteria

Satisfied:

- Existing behavior is documented.
- Current database and receipt file behavior is understood.
- A zip backup exists before modernization.

Needs manual confirmation:

- Run the app from Eclipse using the existing `.classpath`.
- Verify that each role can log in with the intended credentials.
- Confirm whether the ReadMe credentials or database credentials are the source of truth for `bister`.

