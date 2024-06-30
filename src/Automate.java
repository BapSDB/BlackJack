import java.util.Arrays;
import java.util.Collection;

public class Automate {

    protected static class Factory {
        final State[] flyweightsState;
        final Command[] flyweightsCommand;

        protected Factory(int nbState, int nbCommand) {
            this.flyweightsState = new State[nbState];
            this.flyweightsCommand = new Command[nbCommand];
        }

        State creerState(int id) {
            return switch (id) {
                case 1 -> new State10C();
                case 2 -> new State20C();
                case 3 -> new State30C();
                default -> new State0C();
            };
        }

        Command creerCommand(int id) {
            return switch (id) {
                case 0 -> new Command10C();
                case 1 -> new Command20C();
                case 2 -> new CommandThe();
                case 3 -> new CommandCafe();
                default -> new CommandReset();
            };
        }

        private Command getCommand(int id) {
            if (flyweightsCommand[id] == null)
                flyweightsCommand[id] = creerCommand(id);
            return flyweightsCommand[id];
        }


        private State getState(int id) {
            if (flyweightsState[id] == null)
                flyweightsState[id] = creerState(id);
            return flyweightsState[id];
        }
    }

    protected final Factory factory;

    final State getState(int id) {
        return factory.getState(id);
    }

    final Command getCommand(int id) {
        return factory.getCommand(id);
    }

    private State state;

    public Automate(int nbState, int nbCommand) {
        this.factory = new Factory(nbState, nbCommand);
        this.state = factory.getState(State.STATE_0C);
    }

    void insert10Cents() {
        state.insert10Cents(this);
    }

    void insert20Cents() {
        state.insert20Cents(this);
    }

    void pushThe() {
        state.pushThe(this);
    }

    void pushCafe() {
        state.pushCafe(this);
    }

    void pushReset() {
        state.pushReset(this);
    }

    void changeState(State state) {
        this.state = state;
    }

    public static void main(String[] args) {
        var auto = new Automate(State.NB_STATE, Command.NB_COMMAND);
        var command1 = new MacroCommand(
                auto.getCommand(Command.COMMAND_10C),
                auto.getCommand(Command.COMMAND_10C),
                auto.getCommand(Command.COMMAND_20C),
                auto.getCommand(Command.COMMAND_10C),
                auto.getCommand(Command.COMMAND_CAFE),
                auto.getCommand(Command.COMMAND_20C),
                auto.getCommand(Command.COMMAND_THE),
                auto.getCommand(Command.COMMAND_20C),
                auto.getCommand(Command.COMMAND_RESET)
        );

        command1.execute(auto);

        System.out.println("------------------");

        auto.insert10Cents();
        auto.insert10Cents();
        auto.insert20Cents();
        auto.insert10Cents();
        auto.pushCafe();
        auto.insert20Cents();
        auto.pushThe();
        auto.insert20Cents();
        auto.pushReset();
        auto.pushReset();
    }

}

abstract class Command {
    static final int COMMAND_10C = 0;
    static final int COMMAND_20C = 1;
    static final int COMMAND_THE = 2;
    static final int COMMAND_CAFE = 3;
    static final int COMMAND_RESET = 4;
    static final int NB_COMMAND = 5;

    abstract void execute(Automate auto);
}

class MacroCommand extends Command {

    final Collection<Command> commandes;

    public MacroCommand(Collection<Command> commandes) {
        this.commandes = commandes;
    }

    public MacroCommand(Command... commandes) {
        this(Arrays.asList(commandes));
    }

    @Override
    void execute(Automate auto) {
        for (Command command : commandes)
            command.execute(auto);
    }
}

class Command10C extends Command {
    @Override
    void execute(Automate auto) {
        auto.insert10Cents();
    }
}

class Command20C extends Command {
    @Override
    void execute(Automate auto) {
        auto.insert20Cents();
    }
}

class CommandThe extends Command {
    @Override
    void execute(Automate auto) {
        auto.pushThe();
    }
}

class CommandCafe extends Command {
    @Override
    void execute(Automate auto) {
        auto.pushCafe();
    }
}

class CommandReset extends Command {
    @Override
    void execute(Automate auto) {
        auto.pushReset();
    }
}

abstract class State {

    static final int STATE_0C = 0;
    static final int STATE_10C = 1;
    static final int STATE_20C = 2;
    static final int STATE_30C = 3;
    static final int NB_STATE = 4;

    void insert10Cents(Automate auto) {
        System.out.println("Vous ne pouvez pas insérer plus de 30 C !");
    }

    void insert20Cents(Automate auto) {
        System.out.println("Vous ne pouvez pas insérer plus de 30 C !");
    }

    void pushThe(Automate auto) {
        System.out.println("Pas assez d'argent inséré. Le thé coûte 20 C.");
    }

    void pushCafe(Automate auto) {
        System.out.println("Pas assez d'argent inséré. Le café coûte 30 C.");
    }

    void pushReset(Automate auto) {
        System.out.println("Pas d'argent dans le distributeur !");
    }
}

class State0C extends State {
    @Override
    void insert10Cents(Automate auto) {
        System.out.println("CLING ! 10 C");
        auto.changeState(auto.getState(STATE_10C));
    }

    @Override
    void insert20Cents(Automate auto) {
        System.out.println("CLING ! 20 C");
        auto.changeState(auto.getState(STATE_20C));
    }
}

class State10C extends State {
    @Override
    void insert10Cents(Automate auto) {
        System.out.println("CLING ! 20 C");
        auto.changeState(auto.getState(STATE_20C));
    }

    @Override
    void insert20Cents(Automate auto) {
        System.out.println("CLING ! 30 C");
        auto.changeState(auto.getState(STATE_30C));
    }

    @Override
    void pushReset(Automate auto) {
        System.out.println("Je vous rends vos 10 C");
        auto.changeState(auto.getState(STATE_0C));
    }

}

class State20C extends State {
    @Override
    void insert10Cents(Automate auto) {
        System.out.println("CLING ! 30 C");
        auto.changeState(auto.getState(STATE_30C));
    }

    @Override
    void pushThe(Automate auto) {
        System.out.println("Votre thé est servi");
        auto.changeState(auto.getState(STATE_0C));
    }

    @Override
    void pushReset(Automate auto) {
        System.out.println("Je vous rends vos 20 C");
        auto.changeState(auto.getState(STATE_0C));
    }
}

class State30C extends State {
    @Override
    void pushThe(Automate auto) {
        System.out.println("Je vous rends 10 C");
        System.out.println("Votre thé est servi");
        auto.changeState(auto.getState(STATE_0C));
    }

    @Override
    void pushCafe(Automate auto) {
        System.out.println("Votre café est servi");
        auto.changeState(auto.getState(STATE_0C));
    }

    @Override
    void pushReset(Automate auto) {
        System.out.println("Je vous rends vos 30 C");
        auto.changeState(auto.getState(STATE_0C));
    }
}
