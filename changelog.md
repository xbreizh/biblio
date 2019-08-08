## Unreleased


## [0.5.0] 2019-07-30, 13- Added reminder (alerte avant date)
			13- added dao for reminder
			13- added reminder form

## [0.4.0] 2019-07-30, 3- Added fonction Reservation en ligne
			3- added wsdl method addReminder
			3- sending Ready books ok
			3- added wsdl method for loan ready
			3- added Mail method for getting Loans ready to start
			3- added cleanup method and cron job
			3- refactoring
			3- fixed list refresh bug on reserving
			3- added max reserving limit alert
			3- fixed reserve book method
			3- fixed checkIfSimilarLoanPlannedOrInProgress bug
			3- fixed hiding loans issue on reserve click when overdue
			3- fixed bug not display of reservation option when empty loanList
			3- added alert for book alreadyRented
			3- creating list of rented isbn for the view
			3- refactoring
			3- adapted display on webapp for the planned loans
			3- added ISBN on loanTypeOut / member
			3- replaced removeLoan with cancelLoan + refactoring
			3- added reserve method + refactoring  on addLoan
			3- added remove option to library ok
			3- added isCheck on loanTypeOut
			3- added removeLoan method
			3- added display overdue loans when trying to book
			3- removed alert when no overdue
			3- updated dependencies
			3- adding loan from webapp ok
			3- added createArrayFromLoanDates method
			3- added calendar widget
			3- updates loan xsd to add book
			3- fixed tests
			3- added getLoansByIsbn method
			3- added getLoansByIsbn method
			3- added check boolean on loan
			3- added dao methods for reserve
			3- added maxReserve + date for addLoan + methods
			3- added LoanStatus Enum

## [0.3.5] 2019-07-23 ,5- Changed  / Amélioration systèmes de logs / Alertes de mail
			5- removed unused class / updated gitIgnore
			5- added and tested logs for webapp and mailapp + dependency upgrade on mailapp
			5- added log4j configuration

## [0.3.4] 2019-07-12, 11- Refactoring
			11- added lombok on webapp
			11- fixed tests / added lombok
			11- updated dependency versions

## [0.3.3] 2019-07-11, 2- added exception for SOAPFaultException
			2- exception handling if issue connecting to APIs
			2- added 403 custom page

## [0.3.2] 2019-07-11, 4- Added alert pour retard à la connexion
			4- adding popup for overdue (on connection)


## [0.3.1] 2019-07-07, 6- Added Reset password
			6- refactoring on reset password
			6- added condition for login page when already logged in
			6- added safety for preventing superAdmin password reset
			6- fixed bug on member creation(pwd) and updated mail regex
			6- reset password ok design to meliorate
			6- added password reset form
			6- adding pwdSending Method
			6- creating resetPwd in mailApp
			6- added getResetPasswordLink

## [0.2.1] 2019-07-02, 11- Refactoring
			11- adding tests to emailManager
			11- adding tests to mailapp
			11- added tests on connectManager
			11- added tests on memberManager
			11- cleaning smell
			11- added tests on loanManager
			11- added tests on business
			11- adding tests to mailapp
			11- cleaning vulnerabilities and smell
			11- cleaning vulnerabilities and bug
			11- added jacoco to mailapp / dependency cleaning
			11- added profile to services
			11- adding test on bookManager
			11- cleaning
			11- updated wsdl source

## [0.2.0] 2019-06-28 Added addCopy function,10- closes #10 addCopy
			10- added addCopy

## [0.1.1] 2019-06-28 11- Refactoring
			11- added wsdl for local
			11- fixed serviceIntegration tests
			11- workaround for config.properties
			11- updated wsdl source
			11- updated member.xsd
			11- adding tests on model
			11- cleaning bugs
			11- updated wsdl source
			11- added for model
			11- added test dependencies and updated test for model
			11- added test to memberService
			11- cleaning smell
			11- updated regex for name / cleaning smell
			11- adding tests on mailManager
			11- adding tests on bookManager
			11- cleaning smell
			11- adding tests for stringValidatorBooks
			11- fixing resetDb issue
			11- adding tests on updateMember
			11- adding tests on memberTransfert
			11- adding tests on memberManager (enabling disabled methods)
			11- switched from HashMap to Map
			11- fixed bug on getLoansByCriterias
			11- added sql files for integration tests / added db for sonarqube
			11- added sql files for integration tests
			11- renames integration files
			11- adding tests for updateMemberDetails
			11- adding tests for getToken
			11- adding tests for generateToken / checkAdmin
			11- adding tests for getMembersByCriterias / invalidateToken
			11- adding tests for getMembersByCriterias
			11- adding tests for memberUpdate
			11- adding tests for memberInsert
			11- adding tests for stringValidator
			11- cleaning smell
			11- refactoring insertMember
			11- fixing file properties
			11- fixing security issue on regex
			11- adding String Validator
			11- clean smell
			11- added tests on bookManager
			11- added tests on mailManager / removed unused classes
			11- added tests on loanDao
			11- added tests on memberDao
			11- added tests on bookDao
			11- refactored loanByCriterias
			11- added coverage on dao (getAllMembers)
			11- fixed loan by criterias issue (to be refactored)
			11- fixed added ignore case criteria
			11- fixed bug on accessing the webservice
			11- cleaned LoanDao
			11- cleaned MailmanagerImpl
			11- cleaning

## [0.1.0] 2019-06-17 Added Integration continue,8- closes #8
			8- fixing vulnerabilities
			8- adding local profiles
			8- cleaning the smell
			8- added resetDb script on tests
			8- added jacoco on main
			8- cleaned bug and removed vulnerabilities
			8- added jacoco
			8- updated timezone
			8- updated db names/ port

## [0.0.3] 2019-06-11 Fixed Probleme d'initialisation de la base,1- closes #1

## [0.0.2] 2019-06-11 Added Tests unitaires,7- closes #7
           	7- added dependencies for testing
           	7- added tests on model for webapp
           	7- set loan properties from file in static / app working on local
           	7- added integration test classes on web module
           	7- added tests on LoanManagerTest
           	7- added tests on BookServiceTest
           	7- fixed bug on loanServiceImpl and refactored ConnectServiceImpl
           	7- added more tests loanService
           	7- added more tests on mailService and loanService
           	7- added more tests on MemberService
           	7- added tests on addMember, updateMember and removeMember
           	7- fixed issue on Service authentication
           	7- added mocking on ConnectService
           	7- added tests on connectService
           	7- added test profiles
           	7- added updateMember integration test
           	7- added tests on LoanDao + DIYed searchByCriterias (to be refactored)
           	7- added tests on BookDao
           	7- added tests on MemberDao + test db
           	7- connection with DAO ok
           	7- adding loans test
           	7- added tests on BookManager
           	7- added tests on EmailValidator, EmailManager, LoanManager, BookManager
           	7- init business test (checkToken, checkPassword and addMember )
           	7- added test dependencies
           	7- added tests on member + init scripts
           	7- added tests on mail
           	7- added tests on loan
           	7- added junit juniper and test folders / tests on Book ok

## [0.0.1] 2019-05-27 Initial commit