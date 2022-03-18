import SwiftUI
import shared

struct SearchList: View {
    @State var artists: [Artist_] = []
    @State var loading: Bool = false
    @State var error: String = ""
    @State var emptyList: Bool = false
    @State var textInput: String = ""
    var body: some View {
        let bookmark: (String, String) -> Void = { id, name in
            searchListVM?.uiModel.update(action: SearchAction.Bookmark(id: id, name: name))
        }
        let debookmark: (String) -> Void = { id in
            searchListVM?.uiModel.update(action: SearchAction.Debookmark(id: id))
        }
        let pressClear: () -> Void = {
            searchListVM?.uiModel.update(action: SearchAction.ClearSearch())
        }
        let pressEnter: () -> Void = {
            searchListVM?.uiModel.update(action: SearchAction.PressSearch())
        }
        let typeSearch: (String) -> Void = { s in
            searchListVM?.uiModel.update(action: SearchAction.TypedSearch(query: s))
        }
        VStack() {
            SearchInput(
                textInput: $textInput,
                loading: $loading,
                typeSearch: typeSearch,
                pressEnter: pressEnter,
                pressClear: pressClear
            )
            ZStack {
                if(error.isEmpty) {
                    List(artists, id: \.id) { artist in
                        NavigationLink {
                            ArtistDetail(
                                artistId: artist.id,
                                artistName: artist.name
                            )
                        } label: {
                            SearchRow(
                                artist: artist,
                                bookmark: bookmark,
                                debookmark: debookmark
                            )
                                .onAppear {
                                    if artist == artists.last {
                                        searchListVM?.uiModel.update(action: SearchAction.PaginateSearch())
                                    }
                                }
                        }
                    }
                }
                if(emptyList && error.isEmpty) {
                    Text("No results!!")
                        .padding(40)
                }
                if(!error.isEmpty) {
                    Text(error)
                        .foregroundColor(.red)
                        .padding(40)
                }
            }
            Spacer()
        }
        .onAppear {
            searchListVM?.uiModel.stateFlow.collect(
                collector: Collector<SearchUIValues>{ v in
                    artists = v.artists
                    loading = v.loading
                    textInput = v.inputText
                    emptyList = v.hasNoResults && !v.isBeforeFirstSearch
                    error = v.error
                }
            ) { _, e in
                print("finished ui state")
            }
        }
    }
    
}

struct SearchInput: View {
    var textInput: Binding<String>
    var loading: Binding<Bool>
    let typeSearch: (String) -> Void
    let pressEnter: () -> Void
    let pressClear: () -> Void
    var body: some View {
        HStack {
            TextField(
                "Search for an artist...",
                text: textInput,
                onEditingChanged: { b in
                    typeSearch(textInput.wrappedValue)
                },
                onCommit: {
                    typeSearch(textInput.wrappedValue)
                    pressEnter()
                }
            )
            if(!textInput.wrappedValue.isEmpty) {
                Image(systemName: "xmark")
                    .onTapGesture {
                        pressClear()
                        textInput.wrappedValue = ""
                    }
            }
            if(loading.wrappedValue) { ProgressView() }
        }
            .background(Color.white)
            .padding(EdgeInsets(top: 12, leading: 12, bottom: 0, trailing: 12))
    }
    
}


struct SearchRow: View {
    var artist: Artist_
    var bookmark: (String, String) -> Void
    var debookmark: (String) -> Void
    var body: some View {
        HStack {
            Image(systemName: artist.bookmarked ? "bookmark.fill" : "bookmark")
                .opacity(artist.bookmarked == true ? 1.0 : 0.1)
                .onTapGesture {
                    if(artist.bookmarked) {
                        debookmark(artist.id)
                    } else {
                        bookmark(artist.id, artist.name)
                    }
                }
            Text(artist.name)
        }
    }
}
