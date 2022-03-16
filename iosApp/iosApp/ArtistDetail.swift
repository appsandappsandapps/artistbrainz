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
            vm.uiModel.update(action: ArtistDetailAction.SearchYoutube())
        }
        viewOnLastFm = {
            vm.uiModel.update(action: ArtistDetailAction.ViewLastFm())
        }
        bookmark = {
            vm.uiModel.update(action: ArtistDetailAction.Bookmark())
            bookmarked = true
        }
        debookmark = {
            vm.uiModel.update(action: ArtistDetailAction.Debookmark())
            bookmarked = false
        }
        vm.uiModel.stateFlow.collect(
            collector: Collector<ArtistDetailUIValues>{ v in
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
                    HStack(alignment: .center) {
                        Image(systemName: bookmarked ? "bookmark.fill" : "bookmark")
                            .opacity(bookmarked == true ? 1.0 : 0.1)
                            .onTapGesture {
                                if(bookmarked) {
                                    debookmark()
                                } else {
                                    bookmark()
                                }
                            }
                        Text(rating)
                        Spacer()
                    }
                        .padding(5)
                    Text(disambiguous)
                    Divider()
                    Button(action: searchOnYoutube) {
                        Text("Search youtube")
                    }
                    Button(action: viewOnLastFm) {
                        Text("View on last.fm")
                    }
                    Divider()
                    Text(summary)
                }
            }
            .padding(10)
        }
        .onAppear {
            updateArtist()
        }
        .navigationTitle(artistName)
    }
}
