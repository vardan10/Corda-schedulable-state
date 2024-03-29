# Event scheduling

## Two Steps to follow:
  1. State needs to implement ```SchedulableState```<br/>
    Override method ```nextScheduledActivity``` which returns ```ScheduledActivity```.<br/>
    ```nextScheduledActivity``` has default paremeters of current state refrence and [flowLogicRefFactory](https://docs.corda.net/api/kotlin/corda/net.corda.core.flows/-flow-logic-ref-factory/index.html) (which can be used to call flows).<br />
    [Example here](https://github.com/vardan10/Corda-schedulable-state/blob/master/src/main/kotlin/com/heartbeat/state/HeartState.kt#L17)
    
    class HeartState() : SchedulableState {

        override fun nextScheduledActivity(thisStateRef: StateRef, flowLogicRefFactory: FlowLogicRefFactory): ScheduledActivity?      
        {

            return ScheduledActivity(<USE_flowLogicRefFactory_TO_CALL_FLOW_WITH_FLOW_PARAMETERS>, <JAVA_INSTANT>)

        }

    }
    
    
  2. Implement a ```FlowLogic``` to be executed by each node.<br/>
    The FlowLogic must be annotated with ```@SchedulableFlow```.
    
    @InitiatingFlow
    @SchedulableFlow
    class HeartbeatFlow(private val stateRef: StateRef) : FlowLogic<String>() {
        ...
    }

## Note
1. Activities associated with any **consumed states are unscheduled**.
2. Any newly produced states are then queried via the nextScheduledActivity method and **if they do not return null then that activity is scheduled** based on the content of the ScheduledActivity object returned.

[Link to Corda docs](https://docs.corda.net/event-scheduling.html)

<hr/>


# Heartbeat CorDapp

This project is updated version of [Corda Heartbeat Sample Project](https://github.com/corda/samples/tree/release-V3/heartbeat)

This CorDapp is a simple showcase of scheduled activities (i.e. activities started by a node at a specific time without 
direct input from the node owner).

A node starts its heartbeat by calling the `StartHeartbeatFlow`. This creates a `HeartState` on the ledger. This 
`HeartState` has a scheduled activity to start the `HeatbeatFlow` as specified by PARAMETER below.

When the `HeartbeatFlow` runs on scheduled time, it consumes the existing `HeartState` and creates a new `HeartState`. 
The new `HeartState` also has a scheduled activity to start the `HeatbeatFlow` on same scheduled time (can change this in code).

## Running the cordapp:

1. `./gradlew deployNodes` - building may take upto a minute (it's much quicker if you already have the Corda binaries).  
2. `./build/nodes/runnodes`

Go to the CRaSH shell for PartyA, and run the `StartHeatbeatFlow`:

    flow start StartHeartbeatFlow <PARAMETER>
    
    <PARAMETER>: Can pass any one of below three options
        1. Provide Zulu time - Flow will be Scheduled only once @Zulu time
          start StartHeartbeatFlow nextActivityTime: "2019-02-14T04:50:25.510045Z"
        
        2. Provide x Seconds - Flow will run contineously for every x seconds
          start StartHeartbeatFlow nextActivityTime: "5"
        
        3. Provide "null" - Flow will not be scheduled
          start StartHeartbeatFlow nextActivityTime: "null"

If you now start monitoring the node's flow activity...

    flow watch
