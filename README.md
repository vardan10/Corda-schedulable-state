# Event scheduling

2 Steps to follow:
  1. State needs to implement ```SchedulableState```
  2. Implement a ```FlowLogic``` to be executed by each node
    The FlowLogic must be annotated with ```@SchedulableFlow```.

