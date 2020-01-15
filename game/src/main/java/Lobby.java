// import org.jspace.ActualField;
// import org.jspace.RemoteSpace;
// import org.jspace.SequentialSpace;
// import org.jspace.SpaceRepository;

// import java.util.Random;

// public class Lobby extends Thread {
//     SpaceRepository repo;
//     SequentialSpace lobby, IDs;
//     Random rand;


//     public Lobby(SpaceRepository repo){
//         this.repo = repo;
//         addSpaces(repo);

//     }



//     private void addSpaces(SpaceRepository repo) {
//         lobby = new SequentialSpace();
//         IDs = new SequentialSpace();
//         repo.add("lobby", lobby);
//         repo.add("IDs", IDs);
//     }

//     @Override
//     public void run() {
//         while(true){
//             lobby.get(new ActualField("connected"));
//             int id = RandomNumber;
//             lobby.put();


//         }

//     }


// }