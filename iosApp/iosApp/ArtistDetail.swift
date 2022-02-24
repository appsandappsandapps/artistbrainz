import SwiftUI
import shared

struct ArtistDetail: View {
    var artistId: String
    var artistName: String
    @State var searchOnYoutube: () -> Void = {}
    @State var viewOnLastFm: () -> Void = {}
    @State var bookmark: () -> Void = {}
    @State var debookmark: () -> Void = {}
    @State var disambiguous: String = ""
    @State var rating: String = ""
    @State var summary: String = ""
    @State var loading: Bool = true
    @State var error: String = ""
    @State var bookmarked: Bool = true

    func updateArtist() {
        let vm = ArtistDetailViewModel(
            artistId: artistId,
            gotoUrlCallback: { url in
                print("going to \(url)")
                if let url = URL(string: url) {
                   UIApplication.shared.open(url)
                }
            }
        )
        searchOnYoutube = {
            vm.uiState.update(action: ArtistDetailUIState.ActionSearchYoutube())
        }
        viewOnLastFm = {
            vm.uiState.update(action: ArtistDetailUIState.ActionViewLastFm())
        }
        bookmark = {
            vm.uiState.update(action: ArtistDetailUIState.ActionBookmark())
            bookmarked = true
        }
        debookmark = {
            vm.uiState.update(action: ArtistDetailUIState.ActionDebookmark())
            bookmarked = false
        }
        vm.uiState.stateFlow.collect(
            collector: Collector<ArtistDetailUIState.UIValues>{ v in
                loading = v.loading
                summary = v.artist.summary
                rating = "Rating: \(v.artist.rating.value)"
                disambiguous = v.artist.disambiguation
                bookmarked = v.artist.bookmarked
                error = v.error
            }
        ) { _, e in
            print("finished ui state")
        }
    }

    var body: some View {
        ScrollView {
            VStack(alignment:.leading) {
                if(loading) { ProgressView() }
                else if(!error.isEmpty) {
                    Text(error)
                        .foregroundColor(.red)
                        .padding(40)
                } else {
                    Image(systemName: bookmarked ? "bookmark.fill" : "bookmark")
                        .opacity(bookmarked == true ? 1.0 : 0.1)
                        .onTapGesture {
                            if(bookmarked) {
                                debookmark()
                            } else {
                                bookmark()
                            }
                        }
                    Text(disambiguous)
                    Text(rating)
                    Button(action: searchOnYoutube) {
                        Text("Search youtube")
                    }
                    Button(action: viewOnLastFm) {
                        Text("View on last.fm")
                    }
                    Text(summary)
                }
            }
        }
        .onAppear {
            updateArtist()
        }
        .navigationTitle(artistName)
    }
}
