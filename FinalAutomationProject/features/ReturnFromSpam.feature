Feature: Test returning mail from spam

    Scenario: Test return mail from spam folder
        Given I am in mailbox
        When I check first mail
        When I click spam button
        When I open spam folder
        When I return mail from spam
        Then I see message return mail from spam