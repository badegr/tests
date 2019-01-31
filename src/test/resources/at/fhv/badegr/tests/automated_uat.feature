Feature: Automated User Acceptance Tests for the Giphy Randomizer Application

  Background:
    Given Open https://badegr-giphy-staging.herokuapp.com/

  Scenario: Test login and logout
    Given Login with user 'user@test.com' and password 'user123'
    When I press sign out
    Then I see the login page

  Scenario: Test giphy search by search term
    Given Login with user 'user@test.com' and password 'user123'
    When I search for the term 'awesome' and hit enter
    Then I see a gif from giphy
    And I press sign out

  Scenario: Test giphy random search
    Given Login with user 'user@test.com' and password 'user123'
    When I press the random search button
    Then I see a gif from giphy
    And I press sign out
