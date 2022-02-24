import SwiftUI
import shared

struct ContentView: View {
    
    let vm: HomepageViewModel = HomepageViewModel( )
    @State private var selection = 0
    @State private var bookmarks = 0

    var body: some View {
        TabView(selection: $selection) {
            NavigationView {
                SearchList()
                .navigationTitle("Artists")
            }
            .tabItem {
                Image(systemName: "music.note")
                Text("Artists")
            }
            .tag(0)
            NavigationView {
                Bookmarks()
                .navigationTitle("Bookmarks")
            }
            .tabItem {
                Image(systemName: "bookmark.square.fill")
                Text("Bookmarks")
            }
            .tag(1)
            .badge(bookmarks)
            NavigationView {
                About()
                .navigationTitle("About")
            }
            .tabItem {
                Image(systemName: "info.circle.fill")
                Text("About")
            }
            .tag(2)
        }.onAppear {
            vm.uiState.stateFlow.collect(
                collector: Collector<HomepageUIState.UIValues>{ value in
                    bookmarks = Int(value.bookmarked)
                }
            ) { _, e in
                print("finished ui state")
            }
        }
    }
}


struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
