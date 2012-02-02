#!/bin/bash

die() {
	echo $1 >&2
	exit $2
}

usage() {
	case "$#" in
		2)
			echo $1 >&2
			exitstatus=$2
			;;
		1)
			exitstatus=$1
			;;
		0)
			exitstatus=0
			;;
	esac
	echo "Usage: $0 [OPTIONS]"
	echo
	echo "Where OPTIONS are one or more of:"
	echo "	-r (revision | start:end)	Revision or revision range to submit"
	echo "					for review"
	echo "	-h				Output help"
	echo
	echo "Any other args are passed verbatim to RBTools' post-review."
	echo "Useful ones include:"
	echo "	--publish			Immediately publish review request."
	exit $exitstatus
}

# check programs and preconditions
[ -n "$(which git)" ] || die "Cannot find git in PATH" 1
[ -n "$(which post-review)" ] || die "Cannot find post-review in PATH" 2
[ -n "$(git config reviewboard.url)" ] || \
	die "reviewboard.url not set in git config" 3
[ -f ".reviewboardrc" ] || die ".reviewboardrc file not found" 4

# check for 'review' remote
git remote|grep -q review || \
	die "Please add git remote for reviewboard as 'review'" 5

# read options
while [ ! -z "$1" ]; do
	case "$1" in
		-h)
			usage
			;;
		-r)	# revision or revision range
			shift
			[ -n "$1" ] || usage "-r needs an argument" 1
			if echo "$1"|grep -q ':'; then
				# two revs were given
				gitrevarg=$(echo "$1"|awk -F: '{print $1 ".." $2}')
				revarg="--revision-range=$1"
			else
				# only single rev
				gitrevarg="$1^..$1"
				revarg="--revision-range=$(git rev-parse $1^):$1"
			fi
			;;
		*)
			break
			;;
	esac
	shift
done

# update the review remote
git fetch review || die "Could not update 'review' remote" 42

# if revargs not given, use review/master and master
if [ -z "$revarg" ]; then
	# get changesets
	read lastrev firstrev \
		<<< $(git log --format=format:%H review/master..master|tr "\\n" " ")

	if [ -z "$firstrev" ]; then
		firstrev=$(git rev-parse $lastrev^)
	fi
	revarg="--revision-range=$firstrev:$lastrev"
	gitrevarg="$firstrev..$lastrev"
fi

# prompt for okay
git --no-pager log --oneline $gitrevarg
echo "Push these revisions for review?"
select yesno in "Yes" "No"; do
	case $yesno in
		Yes)
			break
			;;
		No)
			exit
			;;
	esac
done

# push to review
git push review master || die "Git push failed" 6

# post review
post-review --guess-summary --guess-description $@ $revarg || \
	die "post-review failed" 7

echo
echo "Done! Don't forget to publish the review request (unless you specified"
echo "--publish)."
