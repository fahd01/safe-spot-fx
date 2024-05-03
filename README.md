# Safe Spot FX
## Requirements
- Java 17
- Install JavaFX plugin for IntelliJ IDEA (includes Scene Builder Kit)
- MySQL (JavaFX app will use the same safe-spot DB created during symfony sprint)

## Run Locally
An IntelliJ IDEA run configuration is provided `Safe Spot FX`

## Tasks
- Place bid
  - [x] Add
  - [ ] Trim last bid
  - [ ] Disable place bid button for ACTIVE loans
  - [ ] Loan changes are not reflected between manage loans and find a loan
- [x] FilteredList does not support add(): change should be done to the source list (Observable list)
- myLoans
  - Bids / Loan
    - [ ] Cover accept, reject buttons state per bid statuses
    - [ ] Accepting / Rejecting bids should change the buttons state
  - Progress bar
    - [ ] Accepting / Rejecting bids should change progress bar
- Remove ID's from tables
  - Popover for loan details
  - Popover for user details
- [ ] Reuse loans.fxml for find a loan and loan management same fxml with include wrapper
- Use the same Status cell for loan and bid
- [ ] deleting bids should be reflected in loan progress
- [ ] Use bootstrap CSS classes to style JavaFX elements
  - add classes from the Scene builder
  - programmatically: `button.getStyleClass().setAll("btn","btn-danger");`
  - elements to style:
    - Buttons
    - Progress bar
    - Tables
- [ ] Add tooltips

## Additional JavaFX libraries
- [bootstrapfx](https://github.com/kordamp/bootstrapfx)
- [controlsfx](https://controlsfx.github.io)
- [FormsFX](https://github.com/dlsc-software-consulting-gmbh/FormsFX/)
- [ValidatorFX](https://github.com/effad/ValidatorFX)
- [tilesfx](https://github.com/HanSolo/tilesfx): A JavaFX library containing tiles for Dashboards.
- [ikonli](https://kordamp.org/ikonli/#_introduction): Icons
## Resources
- [JDBC Workshop](https://gitlab.com/mohamed.hosni.isi/workshopjdbc-3a)
## Git commands
- ``git checkout -b branch_name``
- ``git checkout branch_name``
- ``git branch``checks your current position
- ``git add . `` add files to commit index
- ``git commit -m "commit message " ``
- ``git push origin branch_name``

## Issues
- [TilesFX module issue](https://stackoverflow.com/questions/75250066/javafx-missing-eu-hansolo-module)