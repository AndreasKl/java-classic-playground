package net.andreaskluth.strutssadness.actions

import com.opensymphony.xwork2.ActionSupport;

import spock.lang.Specification;

class SampleSpec extends Specification{

  def "Does the action return success?"(){
    given: "an compostion action"
    def action = new CompositionAction()

    when: "executing the action "
    def result = action.execute()

    then: "all are happy :)="
    result == ActionSupport.SUCCESS
  }

}