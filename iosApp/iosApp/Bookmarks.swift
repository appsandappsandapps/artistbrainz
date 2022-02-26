import SwiftUI
import shared

struct Bookmarks: View {
    let vm: BookmarksViewModel = BookmarksViewModel(
        gotoDetail: { artistId in }
    )
    @State var bookmarks: [Bookmark] = []
    
    var body: some View {
        List(bookmarks, id: \.id) { bookmark in
            NavigationLink {
                ArtistDetail(
                    artistId: bookmark.id,
                    artistName: bookmark.name
                )
            } label: {
                BookmarkRow(
                    bookmark: bookmark,
                    debookmark: { id in
                        vm.uiState.update(action: BookmarksAction.Debookmark(id: id))
                    }
                )
            }
        }.onAppear {
            vm.uiState.stateFlow.collect(
                collector: Collector<BookmarksUIValues>{ v in
                    bookmarks = v.bookmarks
                }
            ) { _, e in
                print("finished ui state")
            }
        }
    }
}

struct BookmarkRow: View {
    var bookmark: Bookmark
    var debookmark: (String) -> Void

    var body: some View {
        HStack {
            Image(systemName:"bookmark.fill")
                .onTapGesture {
                  debookmark(bookmark.id)
                }
            Text(bookmark.name)
        }
    }
}
